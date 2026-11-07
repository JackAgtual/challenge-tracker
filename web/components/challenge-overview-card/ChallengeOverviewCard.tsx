import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { components } from "@/types/api";
import { EditChallengeButton } from "./EditChallengeButton";
import { StartChallengeButton } from "./start-challenge-button";
import { getDisplayNameFromUser } from "@/lib/name-utils";
import { Progress, ProgressLabel, ProgressValue } from "../ui/progress";
import { User } from "../User";

type Props = {
  challenge: components["schemas"]["ChallengeResponse"];
  canEditAndStartChallenge: boolean;
};

export function ChallengeOverviewCard({
  challenge,
  canEditAndStartChallenge,
}: Props) {
  return (
    <Card className="@container">
      <CardHeader>
        <CardTitle>{challenge.name}</CardTitle>
      </CardHeader>
      <CardContent className="@7xl:grid @7xl:grid-cols-2">
        <div className="grid grid-cols-2 gap-y-0.5">
          <p>Status</p>
          <p>{challenge.status}</p>
          <p>Duration (days)</p>
          <p>{challenge.durationDays || "Not set"}</p>
          <p>Start date</p>
          <p>{challenge.starDate || "Not set"}</p>
          <p>Owner</p>
          <User user={challenge.owner} />
        </div>
        <Progress
          value={challenge.completionPercentage * 100}
          className="h-fit my-auto"
        >
          <ProgressLabel>Challenge completion</ProgressLabel>
          <ProgressValue />
        </Progress>
      </CardContent>
      {canEditAndStartChallenge && (
        <CardFooter>
          <EditChallengeButton challengeId={challenge.id} />
          <StartChallengeButton challengeId={challenge.id} />
        </CardFooter>
      )}
    </Card>
  );
}
