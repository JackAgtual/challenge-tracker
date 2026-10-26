import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { components } from "@/types/api";
import { InviteUserForm } from "./InviteUserForm";
import { InvitesTable } from "./InvitesTable";

type Props = {
  invites: components["schemas"]["NonAcceptedInvitesForChallengeResponse"][];
  challengeId: number;
  curUserIsChallengeOwner: boolean;
};

export default function InvitesCard({
  invites,
  challengeId,
  curUserIsChallengeOwner,
}: Props) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Invites</CardTitle>
        <CardDescription>
          <p>
            List of users that have been invited to this challenge. Only the
            challenge owner can invite users to the challenge.
          </p>
        </CardDescription>
      </CardHeader>
      <CardContent>
        {invites.length === 0 ? (
          <div>No invites</div>
        ) : (
          <InvitesTable invites={invites} />
        )}
      </CardContent>
      <CardFooter>
        {curUserIsChallengeOwner && (
          <div>
            <InviteUserForm challengeId={challengeId} />
          </div>
        )}
      </CardFooter>
    </Card>
  );
}
