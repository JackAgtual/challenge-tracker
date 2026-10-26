import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { components } from "@/types/api";
import { EditChallengeButton } from "./EditChallengeButton";
import StartChallengeButton from "./start-challenge-button";
import { getDisplayNameFromUser } from "@/lib/name-utils";

type Props = {
  challenge: components["schemas"]["ChallengeResponse"];
  canEditAndStartChallenge: boolean;
};

export default function ChallengeOverviewCard({
  challenge,
  canEditAndStartChallenge,
}: Props) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>{challenge.name}</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="grid grid-cols-2">
          <p>Status</p>
          <p>{challenge.status}</p>
          <p>Duration (days)</p>
          <p>{challenge.durationDays || "Not set"}</p>
          <p>Start date</p>
          <p>{challenge.starDate || "Not set"}</p>
          <p>Owner</p>
          <p>{getDisplayNameFromUser(challenge.owner)}</p>
        </div>
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
