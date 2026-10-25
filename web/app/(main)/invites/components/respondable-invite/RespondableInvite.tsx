import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { getChallengeNameAndDuration } from "@/lib/name-utils";
import { components } from "@/types/api";
import { InvitedBy } from "../InvitedBy";
import { AcceptButton } from "./AcceptButton";
import { DeclineButton } from "./DeclineButton";

type RespondableInviteProps = {
  invite: components["schemas"]["ReceivedInviteResponse"];
};

export function RespondableInvite({ invite }: RespondableInviteProps) {
  const { id, challenge, inviteSender } = invite;

  return (
    <Card key={id}>
      <CardHeader>
        <CardTitle>{getChallengeNameAndDuration(challenge)}</CardTitle>
      </CardHeader>
      <CardContent>
        <InvitedBy user={inviteSender} />
      </CardContent>
      <CardFooter className="grid grid-cols-2 w-fit gap-x-2">
        <AcceptButton inviteId={id} challengeId={challenge.id} />
        <DeclineButton inviteId={id} />
      </CardFooter>
    </Card>
  );
}
